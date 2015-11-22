package imageanalyzer.filters;

import imageanalyzer.datacontainers.JAIDrawable;
import imageanalyzer.util.JAIHelper;
import imageanalyzer.util.JAIOperators;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import javax.media.jai.JAI;
import javax.media.jai.operator.MedianFilterDescriptor;
import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

/**
 * Created by sereGkaluv on 15-Nov-15.
 */
public class MedianFilter extends DataTransformationFilter<JAIDrawable> {

    private static final String FILTER_NAME = "median";

    private final int _maskSize;

    public MedianFilter(Readable<JAIDrawable> input, Writable<JAIDrawable> output, int maskSize)
    throws InvalidParameterException {
        super(input, output);
        _maskSize = maskSize;
    }

    public MedianFilter(Readable<JAIDrawable> input, int maskSize)
    throws InvalidParameterException {
        super(input);
        _maskSize = maskSize;
    }

    public MedianFilter(Writable<JAIDrawable> output, int maskSize)
    throws InvalidParameterException {
        super(output);
        _maskSize = maskSize;
    }

    @Override
    protected void process(JAIDrawable image) {
        ParameterBlock pb = prepareParameterBlock(image, _maskSize);

        //Creating a new Planar Image according to parameter block
        //and saving the result to JAIDrawable container.
        image.setDrawable(JAI.create(
            JAIOperators.MEDIAN.getOperatorValue(),
            pb
        ));

        //Saving the current process as a file.
        JAIHelper.saveImage(image.getDrawable(), FILTER_NAME);
    }

    /**
     * Prepares parameter block.
     *
     * @param image source image
     * @param maskSize size of the mask that will be used by Median Filter
     * @return New instance of prepared parameter block
     */
    private ParameterBlock prepareParameterBlock(JAIDrawable image, int maskSize) {
        return new ParameterBlock()
            .add(MedianFilterDescriptor.MEDIAN_MASK_PLUS)
            .add(maskSize)
            .addSource(image.getDrawable());
    }
}
