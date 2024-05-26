package zeus.zeushop.service.strategies;

public interface AdministrativeAction<T> {
    boolean execute(T entity);
}
