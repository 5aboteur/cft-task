package filesorter.application;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileSystems;

import org.junit.Test;

import filesorter.sort.SortMode;
import filesorter.util.content.ContentType;

/**
 * Unit tests for AppSettings.
 */
public class AppSettingsBuildTest
{
    @Test
    public void testBuildCorrectAppSettingsSetWorkers()
    {
        AppSettingsBuilder bAppSettings = new AppSettingsBuilder(); 

        bAppSettings.setPath(FileSystems.getDefault().getPath("."));
        bAppSettings.setOutPrefix("pfx");
        bAppSettings.setContentType(ContentType.INTEGER);
        bAppSettings.setSortMode(SortMode.ASCENDING);
        bAppSettings.setNumOfWorkers(6);

        AppSettings settings = bAppSettings.build();

        assertEquals(settings.getPath().toString(), ".");
        assertEquals(settings.getOutPrefix(), "pfx");
        assertEquals(settings.getContentType(), ContentType.INTEGER);
        assertEquals(settings.getSortMode(), SortMode.ASCENDING);
        assertEquals(settings.getNumOfWorkers(), 6);
    }

    @Test
    public void testBuildCorrectAppSettingsNotSetWorkers()
    {
        AppSettingsBuilder bAppSettings = new AppSettingsBuilder(); 

        bAppSettings.setPath(FileSystems.getDefault().getPath("."));
        bAppSettings.setOutPrefix("pfx");
        bAppSettings.setContentType(ContentType.INTEGER);
        bAppSettings.setSortMode(SortMode.ASCENDING);

        AppSettings settings = bAppSettings.build();

        assertEquals(settings.getPath().toString(), ".");
        assertEquals(settings.getOutPrefix(), "pfx");
        assertEquals(settings.getContentType(), ContentType.INTEGER);
        assertEquals(settings.getSortMode(), SortMode.ASCENDING);
        assertEquals(settings.getNumOfWorkers(), settings.getDefaultWorkersNum());
    }
}