/**
 * 
 */
package org.wikimodel.wem;

/**
 * This utility class splits references to individual parts (hyper-link and
 * label) and delegates to separate methods handling of images, normal
 * references and downloads.
 * 
 * @author kotelnikov
 * @author mkirst(at portolancs dot com)
 * @author thomas.mortagne
 */
public abstract class ReferenceHandler {

    public static final String PREFIX_DOWNLOAD = "^(?:d|F)ownload:.*";
    public static final int    PREFIX_DOWNLOAD_LEN = "download:".length();

    public static final String PREFIX_IMAGE = "^(?:i|I)mage:.*";
    public static final int    PREFIX_IMAGE_LEN = "image:".length();

    private boolean supportImage;
    private boolean supportDownload;

    protected ReferenceHandler(boolean supportImage, boolean supportDownload) {
        this.supportImage = supportImage;
        this.supportDownload = supportDownload;
    }
    
    public void handle(WikiReference ref) {
        String link = ref.getLink();
        String label = ref.getLabel();
        WikiParameters params = ref.getParameters();
        
        if (params.getSize() == 0 && label == null) {
            params = params.addParameter("class", "wikimodel-freestanding");
        }
        
        if (this.supportImage && link.matches(PREFIX_IMAGE)) {
            link = link.substring(PREFIX_IMAGE_LEN);
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleImage(link, label, params);
        } else if (this.supportDownload && link.matches(PREFIX_DOWNLOAD)) {
            link = link.substring(PREFIX_DOWNLOAD_LEN);
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleDownload(link, label, params);
        } else {
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleReference(link, label, params);
        }
    }

    protected void handleDownload(
        String ref,
        String label,
        WikiParameters params) {
        handleReference(ref, label, params);
    }

    protected abstract void handleImage(
        String ref,
        String label,
        WikiParameters params);

    protected abstract void handleReference(
        String ref,
        String label,
        WikiParameters params);

}
