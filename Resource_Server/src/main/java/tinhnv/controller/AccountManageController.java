package tinhnv.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tinhnv.dto.account.*;
import tinhnv.service.*;

import java.util.*;

@RestController
@RequestMapping("/account-manage")
public class AccountManageController {

	@Autowired
	IAccountService service;

	@GetMapping
	public ResponseEntity<List<AccountDTO>> all() {
		return ResponseEntity.ok(service.allAccount(false));
	}
	
	@GetMapping("/{pageNo}/{pageSize}")
	public ResponseEntity<List<AccountDTO>> list(@PathVariable("pageNo") Integer pageNo,
			@PathVariable("pageSize") Integer pageSize) {
		return ResponseEntity.ok(service.allAccount(pageNo, pageSize, false));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AccountDTO> one(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.detailAccount(id));
	}
	
	@PostMapping
	public ResponseEntity<AccountDTO> create(@RequestBody DetailAccountDTO account) throws Exception {
		AccountDTO acc = service.createNewAccount(account);
		return ResponseEntity.ok(acc);
	}
	
	@PutMapping
	public ResponseEntity<AccountDTO> update(@RequestBody DetailAccountDTO account) {
		return ResponseEntity.ok(service.updateInformation(account));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		service.deleteAccount(id);
		return ResponseEntity.ok("Tài khoản với mã id: " + id + " đã bị xóa.");
	}
}
