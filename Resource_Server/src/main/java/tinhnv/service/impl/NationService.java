package tinhnv.service.impl;

import com.querydsl.jpa.impl.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import tinhnv.dto.nation.continentdto.*;
import tinhnv.dto.nation.countrydto.*;
import tinhnv.dto.nation.languagedto.*;
import tinhnv.dto.nation.regiondto.*;
import tinhnv.dto.nation.statisticdto.*;
import tinhnv.entity.nation.*;
import tinhnv.entity.nation.builder.*;
import tinhnv.entity.nation.concreate.*;
import tinhnv.entity.nation.idclass.*;
import tinhnv.mapper.*;
import tinhnv.repository.*;
import tinhnv.service.*;
import tinhnv.transfer.*;

import javax.persistence.*;
import java.math.*;
import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class NationService implements INationService {
	
	@Autowired
	ContinentRepository continent;
	@Autowired
	RegionRepository region;
	@Autowired
	CountryRepository country;
	@Autowired
	RegionAreaRepository regionArea;
	@Autowired
	LanguageRepository languageRepo;
	@Autowired
	StatisticRepository statisticRepo;
	@Autowired
	CountryLanguageRepository countryLanguageRepo;
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	CountryMapper countryMapper;
	@Autowired
	RegionMapper regionMapper;
	@Autowired
	ContinentMapper continentMapper;
	@Autowired
	StatisticMapper statisticMapper;

	@Autowired
	LanguageMapper languageMapper;

	private JPAQueryFactory queryFactory;
	
	public NationService() {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
	
	@Override
	public List<ContinentDTOForList> allContinent() {
		return continentMapper.toDTOForList(continent.findAll());
	}

	@Override
	public ContinentDTOForDetail detailContinent(Integer id) {
		Optional<Continent> detail = continent.findById(id);
		if(detail.isPresent()) {
			return continentMapper.toDTOForDetail(detail.get());
		}
		throw new EntityNotFoundException("Không tìm thấy thực thể yêu cầu! <continent with id: " + id + ">");
	}

	@Override
	public List<RegionDTOForList> allRegions() {
		return regionMapper.toDTOForList(region.findAll());
	}

	@Override
	public List<CountryDTOForList> allCountries() {
		return country.findAll().stream()
				.map(countryMapper::toDTOForList)
				.collect(Collectors.toList());
	}

	@Override
	public CountryDTOForDetail detailCountry(Integer id) {
		Optional<Country> detail = country.findById(id);
		if(detail.isPresent()) {
			return countryMapper.toDTOForDetail(detail.get());
		}
		throw new EntityNotFoundException("Không tìm thấy thực thể yêu cầu! <country with id: " + id + ">");
	}

	@Override
	public Language createLanguage(String language) {
		Language newLanguage = new Language(language);
		return languageRepo.save(newLanguage);
	}

	@Override
	public ContinentDTOForList createContinent(String continentName) {
		Continent newContinent = new Continent(continentName);
		return continentMapper.toDTOForList(continent.save(newContinent));
	}

	@Override
	public RegionDTOForList createRegion(String regionName, Integer continentId, BigDecimal area) {
		Continent currentContinent = continent.getById(continentId);
		Region newRegion = new Region(regionName, currentContinent);
		region.save(newRegion);
		RegionArea newRegionArea = new RegionArea(regionName, area);
		regionArea.save(newRegionArea);
		return regionMapper.toDTOForList(newRegion);
	}

	@Override
	public CountryDTOForList createCountry(CountryDTOForCreate countryData, Integer regionId, List<TinyLanguageDTO> listLang) {
		Region currentRegion = region.getById(regionId);
		CountryInformationBuilder builder = new CountryInformationConcreateBuilder();
		Country newCountry = builder.setName(countryData.getCountryName())
				.setArea(countryData.getArea())
				.setNationalDay(countryData.getNationalDay())
				.setCountryCodeTwoChars(countryData.getCountryCodeTwoChars())
				.setCountryCodeThreeChars(countryData.getCountryCodeThreeChars())
				.setRegion(currentRegion)
				.buildEntity();
		List<CountryLanguages> countryLanguages = new ArrayList<>();
		listLang.forEach(lang -> {
			Language language = languageRepo.getById(lang.getLanguageId());
			CountryLanguages countryLan = new CountryLanguages(newCountry, language, lang.isOfficial());
			countryLanguages.add(countryLan);
		});
		newCountry.setLanguages(countryLanguages);
		CountryDTOForList result = countryMapper.toDTOForList(country.save(newCountry));
		countryLanguageRepo.saveAll(countryLanguages);
		return result;
	}

	@Override
	public StatisticDTO createStatistic(StatisticDTO statistic, Integer countryId) {
		Country currentCountry = country.getById(countryId);
		Statistic newStatistic = new Statistic(statistic.getYear(), statistic.getPopulation(), statistic.getGdp(), currentCountry);
		return statisticMapper.toDTO(statisticRepo.save(newStatistic));
	}

	@Override
	public void deleteLanguage(Integer languageId) {
		languageRepo.deleteById(languageId);
	}

	@Override
	public void deleteStatistic(Integer countryId, Integer year) {
		statisticRepo.deleteById(new CountryStatisticId(countryId, year));
	}

	@Override
	public void deleteCountry(Integer countryId) {
		country.deleteById(countryId);
	}

	@Override
	public void deleteRegion(Integer regionId) {
		region.deleteById(regionId);
	}

	@Override
	public void deleteContinent(Integer continentId) {
		continent.deleteById(continentId);
	}

	@Override
	public LanguageDTOCreate updateLanguage(LanguageDTOCreate language) {

		QLanguage qLang = QLanguage.language;
		queryFactory.update(qLang)
				.where(qLang.id.eq(language.getId()))
				.set(qLang.name, language.getName())
				.execute();
		return language;
	}

	@Override
	public ContinentDTOForList updateContinent(ContinentDTOForList continent) {
		QContinent qContinent = QContinent.continent;
		queryFactory.update(qContinent)
		.where(qContinent.id.eq(continent.getContinentId()))
		.set(qContinent.name, continent.getContinentName())
		.execute();
		return continent;
	}

	@Override
	public RegionDTOForList updateRegion(RegionDTOForList region, Integer continentId, BigDecimal area) {
		QRegion qRegion = QRegion.region;
		QRegionArea qRegionArea = QRegionArea.regionArea;
		QContinent qContinent = QContinent.continent;
		queryFactory.update(qRegion)
		.where(qRegion.id.eq(region.getRegionId()))
		.set(qRegion.name, region.getRegionName())
		.execute();
		if(continentId > 0) {
			Continent c = queryFactory.selectFrom(qContinent)
					.where(qContinent.id.eq(continentId))
					.fetchOne();
			queryFactory.update(qRegion)
			.set(qRegion.continent, c)
			.execute();
		}
		if(!area.equals(BigDecimal.ZERO)) {
			queryFactory.update(qRegionArea)
			.where(qRegionArea.name.eq(region.getRegionName()))
			.set(qRegionArea.area, area)
			.execute();
		}
		return region;
	}

	@Override
	public CountryDTOForList updateCountry(CountryDTOForDetail country, Integer regionId) {
		QCountry qCountry = QCountry.country;
		QRegion qRegion = QRegion.region;
		queryFactory.update(qCountry)
		.where(qCountry.id.eq(country.getCountryId()))
		.set(qCountry.name, country.getCountryName())
		.set(qCountry.area, country.getArea())
		.set(qCountry.countryCodeTwoChars, country.getCountryCodeTwoChars())
		.set(qCountry.countryCodeThreeChars, country.getCountryCodeThreeChars())
		.set(qCountry.nationalDay, country.getNationalDay())
		.execute();
		if(regionId > 0) {
			Region r = queryFactory.selectFrom(qRegion)
					.where(qRegion.id.eq(regionId))
					.fetchOne();
			queryFactory.update(qCountry)
			.where(qCountry.id.eq(country.getCountryId()))
			.set(qCountry.region, r)
			.execute();
		}
		return country;
	}

	@Override
	public StatisticDTO updateStatistic(StatisticDTO statistic, Integer countryId) {
		QStatistic qStat = QStatistic.statistic;
		QCountry qCountry = QCountry.country;
		Country c = queryFactory.selectFrom(qCountry)
				.where(qCountry.id.eq(countryId))
				.fetchOne();
		queryFactory.update(qStat)
		.where(qStat.country.eq(c))
		.set(qStat.year, statistic.getYear())
		.set(qStat.population, statistic.getPopulation())
		.set(qStat.gdp, statistic.getGdp())
		.execute();
		return statistic;
	}

	@Override
	public Paging<LanguageDTOCreate> listLanguages(Integer pageNo, Integer pageSize) {
		Pageable page = PageRequest.of(pageNo, pageSize);
		Page<LanguageDTOCreate> languagePage = languageRepo.findAll(page)
				.map(languageMapper::toDTO);
		return Paging.paging(languagePage);
	}

	@Override
	public Paging<CountryDTOForList> listCountries(Integer pageNo, Integer pageSize) {
		Pageable page = PageRequest.of(pageNo, pageSize);
		Page<CountryDTOForList> countryPage = country.findAll(page)
				.map(countryMapper::toDTOForList);
		return Paging.paging(countryPage);
	}

	@Override
	public List<RegionDTOForList> listRegion() {
		return regionMapper.toDTOForList(region.findAll());
	}

	@Override
	public List<ContinentDTOForList> listContinent() {
		return continentMapper.toDTOForList(continent.findAll());
	}


	@Override
	public RegionDTOForDetail detailRegion(Integer id) {
		Optional<Region> detail = region.findById(id);
		RegionDTOForDetail dto = null;
		if(detail.isPresent()) {
			Optional<RegionArea> area = regionArea.findByName(detail.get().getName());

			dto = regionMapper.toDTOForDetail(detail.get());
			dto.setArea(area.map(RegionArea::getArea).orElse(null));
			return dto;
		}
		throw new EntityNotFoundException("<Không tìm thấy thực thể yêu cầu!> region with id: " + id + " was not found.");
	}
}
