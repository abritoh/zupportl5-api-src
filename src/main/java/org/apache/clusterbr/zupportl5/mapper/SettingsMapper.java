package org.apache.clusterbr.zupportl5.mapper;

import org.apache.clusterbr.zupportl5.dto.SettingsDto;
import org.apache.clusterbr.zupportl5.entity.Settings;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SettingsMapper_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1201
* <!-- comment-processor-end -->
 */
public class SettingsMapper {

    public static SettingsDto toDTO(Settings settings) {
        if (settings == null) {
            return null;
        }
        return new SettingsDto(
            settings.getIdKey(),
            settings.getValue(),
            settings.getLabel(),
            settings.getExpiresAtLong(),
            settings.getExpiresAtTime(),
            settings.getLastUpdate()
        );
    }

    public static Settings toEntity(SettingsDto settingsDTO) {
        if (settingsDTO == null) {
            return null;
        }
        Settings settings = new Settings();
        settings.setIdKey(settingsDTO.getIdKey());
        settings.setValue(settingsDTO.getValue());
        settings.setLabel(settingsDTO.getLabel());
        settings.setExpiresAtLong(settingsDTO.getExpiresAtLong());
        settings.setExpiresAtTime(settingsDTO.getExpiresAtTime());
        settings.setLastUpdate(settingsDTO.getLastUpdate());
        return settings;
    }
}
