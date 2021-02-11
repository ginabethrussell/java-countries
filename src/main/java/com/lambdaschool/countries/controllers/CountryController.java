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
        countryList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
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

    //http://localhost:2019/population/total
    @GetMapping(value="/population/total", produces = {"application/json"})
    public ResponseEntity<?> getPopulationTotal()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);

        long population = 0;
        for (Country c : countryList)
        {
            population +=  c.getPopulation();
        }
        String rtnStr = "The Total Population is " + population;

        return new ResponseEntity<>(rtnStr, HttpStatus.OK);
    }

    //http://localhost:2019/population/min
    @GetMapping(value="/population/min", produces = {"application/json"})
    public ResponseEntity<?> getPopulationMin()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);

        countryList.sort((c1, c2) -> (int)(c1.getPopulation() - c2.getPopulation()));
        Country minCountry = countryList.get(0);
        return new ResponseEntity<>(minCountry, HttpStatus.OK);
    }

    //http://localhost:2019/population/max
    @GetMapping(value="/population/max", produces = {"application/json"})
    public ResponseEntity<?> getPopulationMax()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);

        countryList.sort((c1, c2) -> (int)(c2.getPopulation() - c1.getPopulation()));
        Country maxCountry = countryList.get(0);
        return new ResponseEntity<>(maxCountry, HttpStatus.OK);
    }

    //http://localhost:2019/population/median
    @GetMapping(value="/population/median", produces = {"application/json"})
    public ResponseEntity<?> getPopulationMedian()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(countryList::add);

        countryList.sort((c1, c2) -> (int)(c1.getPopulation() - c2.getPopulation()));
        int size = countryList.size();
        System.out.println("Size of table " + size);
        int index = 0;
        if (size % 2 == 0){
            index = size/2;
        }else {
            index = Math.round(size/2);
        }
        Country medianCountry = countryList.get(index);
        return new ResponseEntity<>(medianCountry, HttpStatus.OK);
    }
}
