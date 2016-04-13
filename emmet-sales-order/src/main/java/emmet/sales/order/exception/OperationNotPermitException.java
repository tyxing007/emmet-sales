package emmet.sales.order.exception;

public class OperationNotPermitException extends Exception {

	private static final long serialVersionUID = 1L;
	

	public OperationNotPermitException(String msg){
		super(msg);
	}

}
