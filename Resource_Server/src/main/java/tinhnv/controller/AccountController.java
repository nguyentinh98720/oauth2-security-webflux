package tinhnv.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;
import tinhnv.dto.account.*;
import tinhnv.service.*;

@RestController
public class AccountController {
	
	@Autowired
	IAccountService service;

	@GetMapping("/profiles")
	public ResponseEntity<DetailAccountDTO> profile(@AuthenticationPrincipal UserDetails user) {
		
		DetailAccountDTO account = service.detailAccount(user.getUsername());
		return ResponseEntity.ok(account);
	}
	
	
	@PutMapping("/profiles")
	public ResponseEntity<AccountDTO> updateProfile(@AuthenticationPrincipal UserDetails user,
			@RequestBody DetailAccountDTO account) {
		
		if(!user.getUsername().equalsIgnoreCase(account.getLoginName())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		
		return ResponseEntity.ok(service.updateInformationForUser(account));
	}
	
	
	@DeleteMapping("/deleteAccount")
	public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal UserDetails user,
			@RequestParam(name="loginName", defaultValue="") String loginName) {
		
		if(loginName.length() != 0 && loginName.equals(user.getUsername())) {
			service.deleteAccount(loginName);
			return ResponseEntity.ok("Đã xóa tài khoản.");
		}
		
		else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Không thể xóa tài khoản!");
	}
}
