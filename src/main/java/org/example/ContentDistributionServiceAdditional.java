package org.example;

import lombok.Value;
import org.springframework.lang.NonNull;

public interface ContentDistributionServiceAdditional {

    /**
     * Метод для получения содержимого бандла.
     * В реальном решении содержимое бандлов могло бы храниться в виде отдельных файлов или в NoSQL базе данных
     * в зависимости от количества и размера файлов, необходимости резервирования и т.д.
     *
     * @param uniqueBundleId уникальный сгенерированный идентификатор бандла
     * @return содержимое бандла
     */
    byte[] publicGetBundleData(@NonNull UniqueBundleId uniqueBundleId);

    @Value
    class UniqueBundleId {
        String id;
    }

}
