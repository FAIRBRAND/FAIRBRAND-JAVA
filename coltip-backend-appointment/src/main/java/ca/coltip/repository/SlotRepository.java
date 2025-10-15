package ca.coltip.repository;

import ca.coltip.data.dto.SlotDto;
import ca.coltip.data.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.stream.Stream;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
  @Query(
    """
    SELECT new ca.coltip.data.dto.SlotDto(st) FROM Slot st
    INNER JOIN st.user
    WHERE st.startAt >= :start AND st.endAt <= :end
    ORDER BY st.startAt ASC
    """
  )
  Page<SlotDto> findAllInRange(
    @Param("start") Instant start,
    @Param("end") Instant end,
    Pageable pageable
  );

  @Query(
    """
    SELECT st FROM Slot st
    INNER JOIN st.user
    WHERE st.startAt >= :start AND st.endAt <= :end
    ORDER BY st.startAt ASC
    """
  )
  Stream<Slot> findAllInRange(
    @Param("start") Instant start,
    @Param("end") Instant end
  );
}
