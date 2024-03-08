package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.ImageDto;
import com.restropos.systemshop.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageDtoPopulator extends AbstractPopulator<Image, ImageDto> {
    @Override
    protected ImageDto populate(Image image, ImageDto imageDto) {
        return new ImageDto(image.getImageName(),image.getFolderName(),image.getLink());
    }

    @Override
    public ImageDto generateTarget() {
        return null;
    }
}
