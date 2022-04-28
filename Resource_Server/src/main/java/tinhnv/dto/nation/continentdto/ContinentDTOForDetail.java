package tinhnv.dto.nation.continentdto;

import lombok.*;
import tinhnv.dto.nation.regiondto.*;

import java.util.*;

@NoArgsConstructor @AllArgsConstructor
public class ContinentDTOForDetail extends ContinentDTOForList {

	@Getter @Setter
	private List<RegionDTOForList> regions;

}
