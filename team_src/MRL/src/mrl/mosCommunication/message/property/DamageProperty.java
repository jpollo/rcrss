package mrl.mosCommunication.message.property;

import mrl.mosCommunication.message.MessageFactory;

/**
 * Created with IntelliJ IDEA.
 * User: MRL
 * Date: 5/17/13
 * Time: 5:05 PM
 * Author: Mostafa Movahedi
 */
public class DamageProperty extends AbstractProperty {
    public DamageProperty(int value) {
        super(value);
    }

    @Override
    protected void setPropertyBitSize() {
        setPropertyBitSize(Integer.toBinaryString(MessageFactory.maxDamageToReport - 1).length());
    }
}
