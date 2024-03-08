package com.restropos.systemshop.dto;

import com.restropos.systemimage.constants.FolderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private String imageName;
    private FolderEnum folderName;
    private String link;
}
