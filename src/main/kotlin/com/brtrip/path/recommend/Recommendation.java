package com.brtrip.path.recommend;

import com.brtrip.path.controller.response.PathResponse;
import com.brtrip.path.domain.Path;
import com.brtrip.path.domain.PathCreator;
import com.brtrip.path.domain.PathFinder;
import com.brtrip.path.domain.PathPlace;
import com.brtrip.place.Place;
import com.brtrip.place.PlaceFinder;
import com.brtrip.place.PlaceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class Recommendation {

    @Autowired
    private PathFinder pathFinder;

    @Autowired
    private PlaceFinder placeFinder;

    @Autowired
    private PathCreator pathCreator;

    public List<PathResponse> run(Place place) {
        Set<Set<String>> transactions = getTransactions(place);
        Float minSupport = (float)0.5; // 최소지지도: 0.5
        Apriori apriori = new Apriori(minSupport, transactions);
        apriori.run();

        String metric = "lift";
        Float minLift = (float)1.0; // 최소향상도: 1.0
        AssociationRule associationRule = new AssociationRule(apriori.getResult(), metric, minLift);
        associationRule.run();

        List<AssociationRuleObject> rules = associationRule.getRules();
        Set<String> placeSet = new HashSet<>();
        for (AssociationRuleObject rule : rules) {
            placeSet.addAll(rule.getAntecedent());
            placeSet.addAll(rule.getConsequent());
        }

        List<PlaceRequest> placeRequestList = new ArrayList<>();
        for (String finalPlace : placeSet) {
            Place p = placeFinder.findById(Integer.parseInt(finalPlace));
            PlaceRequest placeReq = new PlaceRequest(p.getLat(), p.getLng(), p.getName(), p.getContent(),
                    p.getPlaceCategories().stream().map(x -> x.getCategory().getName()).toArray(String[]::new));
            placeRequestList.add(placeReq);
        }
        Path path = pathCreator.create(placeRequestList);

        List<Place> places = new ArrayList<>();
        placeRequestList.stream().map(PlaceRequest::toEntity).forEach(places::add);

        List<PathResponse> pathResponses = new ArrayList<>();
        pathResponses.add(PathResponse.of(path, places));

        return pathResponses;
    }

    private Set<Set<String>> getTransactions(Place place) {
        Set<Set<String>> transactions = new HashSet<>();

        List<Path> paths = pathFinder.findByPlace(place);
        for (Path path : paths) {
            List<PathPlace> pathPlaces = path.getPathPlaces();
            Set<String> placeSet = new HashSet<>();
            for (PathPlace pathPlace : pathPlaces) {
                placeSet.add(Objects.requireNonNull(pathPlace.getPlace().getId()).toString());
            }
            transactions.add(placeSet);
        }

        return transactions;
    }
}