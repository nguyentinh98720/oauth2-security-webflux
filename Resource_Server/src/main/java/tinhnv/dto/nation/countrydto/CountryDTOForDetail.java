package tinhnv.dto.nation.countrydto;

import lombok.*;
import tinhnv.dto.nation.languagedto.*;
import tinhnv.dto.nation.regiondto.*;
import tinhnv.dto.nation.statisticdto.*;

import java.util.*;

@Getter @Setter @NoArgsConstructor
public class CountryDTOForDetail extends CountryDTOForCreate {

	private RegionDTOForList region;
	private List<StatisticDTO> statistics;
	private List<LanguageDTO> languages;

}
