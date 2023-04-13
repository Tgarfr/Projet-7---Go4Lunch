package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListFragmentViewModel extends androidx.lifecycle.ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;
    private final LocationRepository locationRepository;

    public RestaurantListFragmentViewModel(@NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository, @NonNull LocationRepository locationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.locationRepository = locationRepository;
    }

    @NonNull
    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantRepository.getRestaurantListLiveData();
    }

    @NonNull
    public LiveData<String> getRestaurantListSearchString() {
        return searchRepository.getRestaurantListFragmentSearchLiveData();
    }

    public List<RestaurantListItem> getListItemRestaurant() {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantListResearchedByString(searchRepository.getRestaurantListFragmentSearchLiveData().getValue());
        Location location = locationRepository.getLocationLiveData().getValue();
        List<RestaurantListItem> restaurantsListItem = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            Location restaurantLocation = new Location(restaurant.getName());
            restaurantLocation.setLatitude(restaurant.getLatitude());
            restaurantLocation.setLongitude(restaurant.getLongitude());
            int distance = 0;
            if (location != null) {
                distance = (int) location.distanceTo(restaurantLocation);
            }
            short numberOfWorkmate = 0; // TODO
            float score = 0; // TODO
            restaurantsListItem.add(new RestaurantListItem(restaurant, distance, numberOfWorkmate, score));
        }
        return restaurantsListItem;
    }
}