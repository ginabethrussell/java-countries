package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController
{
    private List<Country> findCountries(List<Country> countryList, CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();
        for (Country c : countryList)
        {
            if (tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }
    @Autowired
    CountryRepository countryRepository;

    //Routes
    // return all json country objects
    //http://localhost:2019/names/all
    @GetMapping(value="/names/all", produces = {"application/json"})
    public ResponseEntity<?> listAllCountries()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);
        for (Country c : countryList)
        {
            System.out.println(c);
        }
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    //http://localhost:2019/names/start/{letter}
    @GetMapping(value="/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> listCountryByLetter(@PathVariable char letter)
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);

        List<Country> rtnList =  findCountries(countryList, c -> c.getName().charAt(0) == letter || Character.toLowerCase(c.getName().charAt(0)) == letter );

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

}
