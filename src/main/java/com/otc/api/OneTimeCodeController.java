package com.otc.api;

import com.otc.dto.CreateOneTimeCodeRequest;
import com.otc.dto.OneTimeCodeResponse;
import com.otc.dto.UseOneTimeCodeRequest;
import com.otc.mapper.OneTimeCodeMapper;
import com.otc.service.OneTimeCodeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otc")
public class OneTimeCodeController {

    private final OneTimeCodeService service;

    public OneTimeCodeController(OneTimeCodeService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OneTimeCodeResponse create(@Valid @RequestBody CreateOneTimeCodeRequest req) {
        var pass = service.create(req.customerId(), req.bankId(), req.availableLoanLimit(), req.expiryMinutes());
        return OneTimeCodeMapper.toResponse(pass);
    }

    @GetMapping("/{id}")
    public OneTimeCodeResponse get(@PathVariable Long id) {
        return OneTimeCodeMapper.toResponse(service.get(id));
    }

    @PostMapping("/{id}/activate")
    public OneTimeCodeResponse activate(@PathVariable Long id) {
        return OneTimeCodeMapper.toResponse(service.activate(id));
    }

    @PostMapping("/{id}/use")
    public OneTimeCodeResponse use(@PathVariable Long id, @Valid @RequestBody UseOneTimeCodeRequest req) {
        return OneTimeCodeMapper.toResponse(service.useOnce(id, req.amount()));
    }

    @PostMapping("/{id}/cancel")
    public OneTimeCodeResponse cancel(@PathVariable Long id) {
        return OneTimeCodeMapper.toResponse(service.cancel(id));
    }
}
