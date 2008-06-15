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
 */
public abstract class ReferenceHandler {

    public static final String PREFIX_DOWNLOAD = "download:";

    public static final String PREFIX_IMAGE = "image:";

    public void handle(String ref) {
        ref = ref.trim();
        int idx = ref.indexOf(' ');
        String label = "";
        if (idx > 0) {
            label = ref.substring(idx).trim();
            ref = ref.substring(0, idx);
        }
        handle(ref, label);
    }

    /**
     * @param link
     * @param label
     */
    private void handle(String link, String label) {
        if (link.startsWith(PREFIX_IMAGE)) {
            link = link.substring(PREFIX_IMAGE.length());
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleImage(link, label);
        } else if (link.startsWith(PREFIX_DOWNLOAD)) {
            link = link.substring(PREFIX_DOWNLOAD.length());
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleDownload(link, label);
        } else {
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleReference(link, label);
        }
    }

    public void handle(WikiReference ref) {
        handle(ref.getLink(), ref.getLabel());
    }

    protected void handleDownload(String ref, String label) {
        handleReference(ref, label);
    }

    protected abstract void handleImage(String ref, String label);

    protected abstract void handleReference(String ref, String label);

}
