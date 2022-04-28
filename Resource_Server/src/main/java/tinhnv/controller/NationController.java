package tinhnv.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tinhnv.dto.nation.continentdto.*;
import tinhnv.dto.nation.countrydto.*;
import tinhnv.dto.nation.regiondto.*;
import tinhnv.service.*;

import java.util.*;

@RestController
@RequestMapping("/api/nation")
public class NationController {
	
	@Autowired
	INationService service;

	@GetMapping("/continents")
	public ResponseEntity<List<ContinentDTOForList>> allContinents() {
		List<ContinentDTOForList> models = service.allContinent();
		return ResponseEntity.ok(models);
	}
	
	@GetMapping("/continents/{id}")
	public ResponseEntity<ContinentDTOForDetail> oneContinent(@PathVariable Integer id) {
		ContinentDTOForDetail continent = service.detailContinent(id);
		return ResponseEntity.ok(continent);
	}
	
	@GetMapping("/regions")
	public ResponseEntity<List<RegionDTOForList>> allRegions() {
		List<RegionDTOForList> models = service.allRegions();
		return ResponseEntity.ok(models);
	}
	
	@GetMapping("/regions/{id}")
	public ResponseEntity<RegionDTOForDetail> oneRegion(@PathVariable Integer id) {
		RegionDTOForDetail region = service.detailRegion(id);
		return ResponseEntity.ok(region);
	}
	
	@GetMapping("/countries")
	public ResponseEntity<List<CountryDTOForList>> allCountries() {
		List<CountryDTOForList> models = service.allCountries();
		return ResponseEntity.ok(models);
	}
	
	@GetMapping("/countries/{id}")
	public ResponseEntity<CountryDTOForDetail> oneCountry(@PathVariable Integer id) {
		CountryDTOForDetail country = service.detailCountry(id);
		return ResponseEntity.ok(country);
	}
}
