package io.student.rococo.service.api;

import io.student.rococo.data.repository.CountryRepository;
import io.student.rococo.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Page<CountryJson> allCountries(Pageable pageable) {
        return countryRepository.findAll(pageable).map(countryEntity ->
            new CountryJson(
                    countryEntity.getId(),
                    countryEntity.getName()));

    }
}
