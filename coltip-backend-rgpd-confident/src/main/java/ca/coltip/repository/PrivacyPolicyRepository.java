package ca.coltip.repository;

import ca.coltip.data.PrivacyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyPolicyRepository extends JpaRepository <PrivacyPolicy, Long> { }
