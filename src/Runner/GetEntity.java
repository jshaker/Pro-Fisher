package Runner;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GameEntity;

public interface GetEntity {
    GameEntity Get(APIContext apiContext);
}
