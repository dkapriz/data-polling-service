package org.example.iniconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.example.iniconfig.dto.IniConfigDto;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class IniConfig {

    public static final String DEFAULT_FILE_NAME = "config.ini";
    public static final int MAX_STRING_LENGTH = 2;
    public static final int MIN_INTERVAL = 5;
    public static final String EX_MSG_WRONG_FIELD_VALUE = "Invalid field value: ";
    IniConfigDto config;

    /**
     * Метод читает файл конфигурации с расширением ini и создает POJO объект конфигурации.
     * Производится проверка следующих полей файла:
     * [ServerSettings]
     * Collector=<String>
     * Port=<String>
     * Interval=<Integer>
     * Console_encoding=<String>
     * [MetricSettings]
     * Ping_localhost=<String>
     *
     * @param filePath - имя файла или путь к файлу конфигурации
     * @return null - файл прочитан с ошибками или не прошел валидацию
     * IniConfigDto - успешное чтение файла
     */
    public IniConfigDto readFile(String filePath) {
        INIConfiguration iniConfiguration = new INIConfiguration();
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            iniConfiguration.read(fileReader);
            getIniConfig(iniConfiguration);
            iniConfiguration.clear();
            fileReader.close();
            checkConfigFile();
            log.info("The configuration file has been read successfully");
        } catch (IOException e) {
            log.error("Error reading the configuration INI file");
            config = null;
        } catch (ConfigurationException e) {
            log.error("Error in the configuration of the INI file");
            config = null;
        }
        return config;
    }

    public void readFile() {
        readFile(DEFAULT_FILE_NAME);
    }

    private void getIniConfig(INIConfiguration iniConfiguration) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> iniKeys = parseIniFile(iniConfiguration);
        config = objectMapper.convertValue(iniKeys, IniConfigDto.class);
    }

    private Map<String, Map<String, String>> parseIniFile(INIConfiguration iniConfiguration) {
        Map<String, Map<String, String>> iniFileContents = new HashMap<>();
        for (String section : iniConfiguration.getSections()) {
            Map<String, String> subSectionMap = new HashMap<>();
            SubnodeConfiguration confSection = iniConfiguration.getSection(section);
            Iterator<String> keyIterator = confSection.getKeys();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                String value = confSection.getProperty(key).toString();
                subSectionMap.put(key, value);
            }
            iniFileContents.put(section, subSectionMap);
        }
        return iniFileContents;
    }

    private void checkConfigFile() throws ConfigurationException {
        if (config.getServerSettings().getCollector().isEmpty() ||
                config.getServerSettings().getCollector().length() <= MAX_STRING_LENGTH) {
            throw new ConfigurationException(EX_MSG_WRONG_FIELD_VALUE + "Collector");
        }
        if (config.getServerSettings().getPort().isEmpty() ||
                Integer.parseInt(config.getServerSettings().getPort()) <= 0) {
            throw new ConfigurationException(EX_MSG_WRONG_FIELD_VALUE + "Port");
        }
        if (config.getServerSettings().getInterval() <= MIN_INTERVAL) {
            throw new ConfigurationException(EX_MSG_WRONG_FIELD_VALUE + "Interval");
        }
        if (config.getServerSettings().getConsoleEncoding().isEmpty() ||
                config.getServerSettings().getConsoleEncoding().length() <= MAX_STRING_LENGTH) {
            throw new ConfigurationException(EX_MSG_WRONG_FIELD_VALUE + "Console_encoding");
        }
        if (config.getMetricSettings().getPingLocalhost().isEmpty() ||
                config.getMetricSettings().getPingLocalhost().length() <= MAX_STRING_LENGTH) {
            throw new ConfigurationException(EX_MSG_WRONG_FIELD_VALUE + "Console_encoding");
        }
    }
}
