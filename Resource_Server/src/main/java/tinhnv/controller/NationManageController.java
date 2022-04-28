package tinhnv.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tinhnv.dto.nation.continentdto.*;
import tinhnv.dto.nation.countrydto.*;
import tinhnv.dto.nation.languagedto.*;
import tinhnv.dto.nation.regiondto.*;
import tinhnv.dto.nation.statisticdto.*;
import tinhnv.entity.nation.*;
import tinhnv.service.*;
import tinhnv.transfer.*;

import java.math.*;
import java.util.*;

@RestController
@RequestMapping("/nation-manage")
public class NationManageController {

	@Autowired
	INationService service;
	
	ObjectMapper mapper;
	
	public NationManageController() {
		mapper = new ObjectMapper();
	}
	
	@GetMapping("/languages/list")
	public ResponseEntity<Paging<LanguageDTOCreate>> listLanguages(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		Paging<LanguageDTOCreate> page = service.listLanguages(pageNo, pageSize);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/countries/list")
	public ResponseEntity<Paging<CountryDTOForList>> listCountries(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		Paging<CountryDTOForList> page = service.listCountries(pageNo, pageSize);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/regions/list")
	public ResponseEntity<List<RegionDTOForList>> listRegions() {
		return ResponseEntity.ok(service.listRegion());
	}
	
	@GetMapping("/continents/list")
	public ResponseEntity<List<ContinentDTOForList>> listContinents() {
		return ResponseEntity.ok(service.listContinent());
	}
	
	@PostMapping("/languages")
	public ResponseEntity<Language> createLanguage(@RequestParam String language) {
		return ResponseEntity.ok(service.createLanguage(language));
	}
	
	@PostMapping("/continents")
	public ResponseEntity<ContinentDTOForList> createContinent(@RequestParam String name) {
		return ResponseEntity.ok(service.createContinent(name));
	}
	
	@PostMapping("/regions")
	public ResponseEntity<RegionDTOForList> createRegion(
			@RequestBody ObjectNode data) {
			String name = data.get("name").asText();
			Integer continentId = data.get("continentId").asInt();
			BigDecimal area = new BigDecimal(data.get("area").asText("0"));
		return ResponseEntity.ok(service.createRegion(name, continentId, area));
	}
	
	@PostMapping("/countries")
	public ResponseEntity<CountryDTOForList> createCountry(
			@RequestBody ObjectNode data)
			throws JsonMappingException, JsonProcessingException {
		CountryDTOForCreate country = mapper.treeToValue(data.get("country"), CountryDTOForCreate.class);
		Integer regionId = data.get("regionId").asInt();
		ArrayNode languageNode = (ArrayNode) data.get("languages");
		List<TinyLanguageDTO> languages = new ArrayList<>();
		languageNode.forEach(lang -> {
			try {
				languages.add(mapper.treeToValue(lang, TinyLanguageDTO.class));
			} catch (JsonProcessingException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		});
		return ResponseEntity.ok(service.createCountry(country, regionId, languages));
	}
	
	@PostMapping("/statistic")
	public ResponseEntity<StatisticDTO> createStatistic(
			@RequestBody ObjectNode data)
			throws JsonMappingException, JsonProcessingException {
		StatisticDTO statistic = mapper.treeToValue(data.get("statistic"), StatisticDTO.class);
		Integer countryId = data.get("countryId").asInt();
		return ResponseEntity.ok(service.createStatistic(statistic, countryId));
	}
	
	@PutMapping("/languages")
	public ResponseEntity<LanguageDTOCreate> updateLanguage(@RequestBody LanguageDTOCreate language) {
		return ResponseEntity.ok(service.updateLanguage(language));
	}
	
	@PutMapping("/continents")
	public ResponseEntity<ContinentDTOForList> updateContinent(@RequestBody ContinentDTOForList continent) {
		return ResponseEntity.ok(service.updateContinent(continent));
	}
	
	@PutMapping("/regions")
	public ResponseEntity<RegionDTOForList> updateRegion(
			@RequestBody ObjectNode data) throws JsonProcessingException, IllegalArgumentException {
		RegionDTOForList region = mapper.treeToValue(data.get("region"), RegionDTOForList.class);
		Integer continentId = data.get("continentId").asInt();
		BigDecimal area = new BigDecimal(data.get("area").asText("0"));
		return ResponseEntity.ok(service.updateRegion(region, continentId, area));
	}
	
	@PutMapping("/countries")
	public ResponseEntity<CountryDTOForList> updateCountry(
			@RequestBody ObjectNode data) throws JsonProcessingException, IllegalArgumentException {
		CountryDTOForDetail c = mapper.treeToValue(data.get("country"), CountryDTOForDetail.class);
		Integer regionId = data.get("regionId").asInt(0);
		return ResponseEntity.ok(service.updateCountry(c, regionId));
	}
	
	@PutMapping("/statistic")
	public ResponseEntity<StatisticDTO> updateStatistic(
			@RequestBody ObjectNode data)
			throws JsonProcessingException, IllegalArgumentException {
		StatisticDTO s = mapper.treeToValue(data.get("statistic"), StatisticDTO.class);
		Integer countryId = data.get("countryId").asInt(0);
		return ResponseEntity.ok(service.updateStatistic(s, countryId));
	}
	
	@DeleteMapping("/languages/{id}")
	public ResponseEntity<Integer> deleteLanguage(@PathVariable Integer id) {
		service.deleteLanguage(id);
		return ResponseEntity.ok(id);
	}
	
	@DeleteMapping("/continents/{id}")
	public ResponseEntity<Integer> deleteContinent(@PathVariable Integer id) {
		service.deleteContinent(id);
		return ResponseEntity.ok(id);
	}
	
	@DeleteMapping("/countries/{id}")
	public ResponseEntity<Integer> deleteCountry(@PathVariable Integer id) {
		service.deleteCountry(id);
		return ResponseEntity.ok(id);
	}
	
	@DeleteMapping("/statistic/{countryId}/{year}")
	public ResponseEntity<String> deleteStatistic(@PathVariable Integer countryId, @PathVariable Integer year) {
		service.deleteStatistic(countryId, year);
		return ResponseEntity.ok(countryId + " - " + year);
	}
	
	@DeleteMapping("/regions/{id}")
	public ResponseEntity<Integer> deleteRegion(@PathVariable Integer id) {
		service.deleteRegion(id);
		return ResponseEntity.ok(id);
	}
}
