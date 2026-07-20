package io.student.rcc.data.tpl;

import com.atomikos.icatch.jta.UserTransactionImp;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class XaTransactionTemplate {

    private final JdbcConnectionHolders holders;
    private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public XaTransactionTemplate(String... jdbcUrl) {
        this.holders = Connections.holders(jdbcUrl);
    }

    public XaTransactionTemplate holdConnectionAfterAction() {
        this.closeAfterAction.set(false);
        return this;
    }


    @SafeVarargs
    public final @Nullable <T> T execute(Supplier<T>... actions) {
        UserTransaction ut = new UserTransactionImp();

        try {
            ut.begin();

            T result = null;
            for (Supplier<T> action : actions) {
                result = action.get();
            }

            ut.commit();
            return result;
        } catch (Exception originalException) {
            rollbackIfNecessary(ut, originalException);
            throw new RuntimeException(
                "Ошибка выполнения XA-транзакции",
                originalException
            );
        } finally {
            if (closeAfterAction.get()) {
                holders.close();
            }
        }
    }

    private void rollbackIfNecessary(
        UserTransaction ut,
        Exception originalException
    ) {
        try {
            int status = ut.getStatus();

            if (status == Status.STATUS_ACTIVE
                || status == Status.STATUS_MARKED_ROLLBACK) {
                ut.rollback();
            }
        } catch (Exception rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
