package org.example.content;

import org.example.ContentDistributionService;
import org.example.ContentDistributionServiceAdditional;
import org.springframework.lang.NonNull;

public interface BundleRepository {

    /**
     * Метод создания бандла.
     * Для одной комбинации publishListId и bundleId может быть только один бандл.
     *
     * @param publishListId идентификатор СНП
     * @param bundleId      идентификатор бандла
     * @param data          содержимое бандла
     * @return уникальный идентификатор бандла, соответствующий переданной комбинации publishListId и bundleId
     */
    ContentDistributionServiceAdditional.UniqueBundleId createBundle(
            @NonNull ContentDistributionService.PublishListId publishListId,
            @NonNull ContentDistributionService.BundleId bundleId,
            @NonNull byte[] data);

    /**
     * Метод получения содержимого бандла.
     *
     * @param uniqueBundleId уникальный сгенерированный идентификатор бандла
     * @return содержимое бандла
     */
    byte[] getBundleContent(@NonNull ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId);

}
