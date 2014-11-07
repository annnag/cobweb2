package production;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import cobweb.Environment;
import cobweb.Scheduler;
import cobweb.ViewerClosedCallback;
import cobweb.ViewerPlugin;
import cwcore.ComplexEnvironment;

public class ProductionViewer implements ViewerPlugin {

	private Disp productionDisplay;
	private ViewerClosedCallback onClosed;

	private ComplexEnvironment theEnvironment;
	private Scheduler theScheduler;


	public ProductionViewer(Environment theEnvironment, Scheduler theScheduler) {
		super();
		this.theEnvironment = (ComplexEnvironment) theEnvironment;
		this.theScheduler = theScheduler;
	}

	@Override
	public void on() {
		ProductionMapper newMapper = (theEnvironment).prodMapper;
		Disp jd = newMapper.createDialog();

		if (productionDisplay != null) {
			theScheduler.removeSchedulerClient(productionDisplay);
			productionDisplay.setVisible(false);
			productionDisplay.setEnabled(false);
			productionDisplay.dispose();
		}

		productionDisplay = jd;

		jd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onClosed.viewerClosed();
			}
		});

		theScheduler.addSchedulerClient(productionDisplay);
	}

	@Override
	public void off() {
		if (productionDisplay != null) {
			theScheduler.removeSchedulerClient(productionDisplay);
			productionDisplay.setVisible(false);
			productionDisplay.setEnabled(false);
			productionDisplay.dispose();
		}
		productionDisplay = null;
	}

	@Override
	public String getName() {
		return "Production map";
	}

	@Override
	public void setClosedCallback(ViewerClosedCallback onClosed) {
		this.onClosed = onClosed;

	}
}