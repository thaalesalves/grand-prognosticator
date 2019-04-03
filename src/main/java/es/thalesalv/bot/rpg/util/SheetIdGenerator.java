package es.thalesalv.bot.rpg.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SheetIdGenerator implements IdentifierGenerator {

    public static final String generatorName = "sheetIdGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
