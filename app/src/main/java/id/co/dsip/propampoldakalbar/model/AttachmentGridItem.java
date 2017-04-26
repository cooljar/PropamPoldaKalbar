package id.co.dsip.propampoldakalbar.model;

/**
 * Created by japra_awok on 14/04/2017.
 */

public class AttachmentGridItem {
    public long id;
    public String file_type, mime_type, original_path, thumb_path, file_name;

    public AttachmentGridItem() {
    }

    public AttachmentGridItem(long id, String file_type, String mime_type, String original_path, String thumb_path, String file_name) {
        this.id = id;
        this.file_type = file_type;
        this.mime_type = mime_type;
        this.original_path = original_path;
        this.thumb_path = thumb_path;
        this.file_name = file_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getOriginal_path() {
        return original_path;
    }

    public void setOriginal_path(String original_path) {
        this.original_path = original_path;
    }

    public String getThumb_path() {
        return thumb_path;
    }

    public void setThumb_path(String thumb_path) {
        this.thumb_path = thumb_path;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
