package ca.coltip.controller;

import ca.coltip.data.PrivacyPolicy;
import ca.coltip.service.PrivacyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/privacy")
public class PrivacyPolicyController {
    @Autowired
    private PrivacyPolicyService service;

    @GetMapping
    public List<PrivacyPolicy> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PrivacyPolicy getById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping
    public PrivacyPolicy create(@RequestBody PrivacyPolicy policy, @RequestParam String author) {
        return service.create(policy, author);
    }

    @PutMapping("/{id}")
    public PrivacyPolicy update(@PathVariable Long id, @RequestBody PrivacyPolicy policy,@RequestParam String edit, @RequestParam String author) {
        return service.update(id, policy, edit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
