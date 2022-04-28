package tinhnv.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterForm {

	private String loginName;
	private String password;
	private String repeatPass;
	private String fullName;
	private String address;
}
