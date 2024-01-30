package com.loan.hero.admin;

import com.loan.hero.loan_officer.data.dto.request.InviteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Super Admin Controller")
@RequestMapping("api/v1/super")
public class SuperAdminController {
    private final SuperAdminServiceImpl superAdminService;

    @PostMapping("invite")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @Operation(summary = "Invite Loan Officer")
    public ResponseEntity<String> inviteLoanOfficer(
            @RequestBody @Valid InviteRequest request
    ) {
        return ResponseEntity.ok(superAdminService.inviteAdmin(request));
    }
}
