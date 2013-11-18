package mrl.mosCommunication.message.property;

import mrl.mosCommunication.entities.PositionTypes;

/**
 * Created with IntelliJ IDEA.
 * User: MRL
 * Date: 5/17/13
 * Time: 2:17 PM
 * Author: Mostafa Movahedi
 * To change this template use File | Settings | File Templates.
 */
public class PositionTypeProperty extends AbstractProperty {

    public PositionTypeProperty(int value) {
        super(value);
    }

    @Override
    protected void setPropertyBitSize() {
        setPropertyBitSize(PositionTypes.getPositionTypeBitNum());
    }
}
