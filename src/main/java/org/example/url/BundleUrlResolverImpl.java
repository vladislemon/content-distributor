package org.example.url;

import org.example.ContentDistributionServiceAdditional;
import org.springframework.stereotype.Component;

@Component
public class BundleUrlResolverImpl implements BundleUrlResolver {

    @Override
    public String getBundleUrlPattern() {
        return "/bundle/get/" + getBundleUrlIdPlaceholder();
    }

    @Override
    public String getBundleUrlIdPlaceholder() {
        return ":uniqueBundleId";
    }

    @Override
    public String getBundleUrl(ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId) {
        return getBundleUrlPattern().replaceAll(getBundleUrlIdPlaceholder(), uniqueBundleId.getId());
    }
}
