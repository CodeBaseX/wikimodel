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

    public void handle(WikiReference ref) {
        String link = ref.getLink();
        String label = ref.getLabel();
        WikiParameters params = ref.getParameters();
        if (link.startsWith(PREFIX_IMAGE)) {
            link = link.substring(PREFIX_IMAGE.length());
            if (label == null || "".equals(label)) {
                label = link;
            }
            handleImage(link, label, params);
        } else if (link.startsWith(PREFIX_DOWNLOAD)) {
            link = link.substring(PREFIX_DOWNLOAD.length());
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
