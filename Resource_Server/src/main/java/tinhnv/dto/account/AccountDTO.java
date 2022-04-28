package tinhnv.dto.account;

import lombok.*;
import org.springframework.stereotype.*;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AccountDTO {

	private Long id;
	private String loginName;
	private String fullName;
	private String role;
	private boolean active;
}
