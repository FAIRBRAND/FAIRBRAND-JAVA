package ca.coltip.service;

import ca.coltip.data.PrivacyPolicy;
import ca.coltip.repository.PrivacyPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrivacyPolicyService {
    @Autowired
    private PrivacyPolicyRepository repository;

    public List<PrivacyPolicy> findAll() {
        return repository.findAll();
    }

    public PrivacyPolicy findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Politique non trouvée avec id: " + id));
    }

    public PrivacyPolicy create(PrivacyPolicy policy, String author) {
        policy.setAuthor(author);
        policy.setLastModifiedBy(author);
        LocalDateTime now = LocalDateTime.now();
        if (policy.getCreatedAt() == null) {
            policy.setCreatedAt(now);
        }
        policy.setUpdatedAt(LocalDateTime.now());
        return repository.save(policy);
    }

    public PrivacyPolicy update(Long id, PrivacyPolicy updatedPolicy, String edit) {
        PrivacyPolicy policy = findById(id);
        policy.setTitle(updatedPolicy.getTitle());
        policy.setDescription(updatedPolicy.getDescription());
        policy.setLastModifiedBy(edit);
        return repository.save(policy);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
