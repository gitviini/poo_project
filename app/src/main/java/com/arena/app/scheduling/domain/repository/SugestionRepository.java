package com.arena.app.scheduling.domain.repository;

import com.arena.app.scheduling.domain.model.Sugestion;
import java.util.UUID;

public interface SugestionRepository {
    Sugestion save(Sugestion sugestion);
}
