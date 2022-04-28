package tinhnv.dto.account;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class DetailAccountDTO extends AccountDTO {

	private String password;
	private String address;
	
	private String note;
}
