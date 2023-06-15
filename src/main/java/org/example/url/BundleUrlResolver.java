package org.example.url;

import org.example.ContentDistributionServiceAdditional;
import org.springframework.lang.NonNull;

public interface BundleUrlResolver {

    /**
     * @return шаблон URL бандла
     */
    String getBundleUrlPattern();

    /**
     * @return placeholder в URL для идентификатора бандла
     */
    String getBundleUrlIdPlaceholder();

    /**
     * Метод получения URL для бандла.
     *
     * @param uniqueBundleId уникальный сгенерированный идентификатор бандла
     * @return URL для бандла
     */
    String getBundleUrl(@NonNull ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId);

}
