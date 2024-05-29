package at.technikum.tourplanner.controller;

import at.technikum.tourplanner.service.MapApi;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "addresses")
public class AddressController {
    @Autowired
    private MapApi mapApi;

//    @GetMapping("/{text}")
//    public List<TourDto> findAddresses(@PathVariable String text) {
//        return mapApi.searchAddress(text);
//    }
}
