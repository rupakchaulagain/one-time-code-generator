package com.otc.api;

import com.otc.api.dto.CreateEcomPassRequest;
import com.otc.api.dto.EcomPassResponse;
import com.otc.api.dto.UseEcomPassRequest;
import com.otc.service.EcomPassService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecompass")
public class EcomPassController {

    private final EcomPassService service;

    public EcomPassController(EcomPassService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EcomPassResponse create(@Valid @RequestBody CreateEcomPassRequest req) {
        var pass = service.create(req.customerId(), req.bankId(), req.availableLoanLimit(), req.expiryMinutes());
        return EcomPassMapper.toResponse(pass);
    }

    @GetMapping("/{id}")
    public EcomPassResponse get(@PathVariable Long id) {
        return EcomPassMapper.toResponse(service.get(id));
    }

    @PostMapping("/{id}/activate")
    public EcomPassResponse activate(@PathVariable Long id) {
        return EcomPassMapper.toResponse(service.activate(id));
    }

    @PostMapping("/{id}/use")
    public EcomPassResponse use(@PathVariable Long id, @Valid @RequestBody UseEcomPassRequest req) {
        return EcomPassMapper.toResponse(service.useOnce(id, req.amount()));
    }

    @PostMapping("/{id}/cancel")
    public EcomPassResponse cancel(@PathVariable Long id) {
        return EcomPassMapper.toResponse(service.cancel(id));
    }
}
