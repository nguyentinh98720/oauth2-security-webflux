package tinhnv.dto.nation.regiondto;

import lombok.*;
import tinhnv.dto.nation.continentdto.*;
import tinhnv.dto.nation.countrydto.*;

import java.math.*;
import java.util.*;

@Getter @Setter @NoArgsConstructor
public class RegionDTOForDetail extends RegionDTOForList {

	private ContinentDTOForList continent;
	
	private BigDecimal area;

	private List<CountryDTOForList> countries;

}
