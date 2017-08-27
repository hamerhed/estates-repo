package pl.hamerhed.scrapper;

public abstract class AbstractItemExtractor<T, R> extends AbstractExtractor<R> implements ItemExtractor<T, R> {

	protected AbstractItemExtractor() {
		super();
	}

}
