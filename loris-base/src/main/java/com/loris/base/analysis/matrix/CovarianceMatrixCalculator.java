package com.loris.base.analysis.matrix;

import Jama.Matrix;

public interface CovarianceMatrixCalculator
{
	/** Calculate covariance matrix of the given data
	 * @param centeredData data matrix where rows are the instances/samples and 
	 * columns are dimensions. It has to be centered.
	 */
	EVDResult run(Matrix centeredData);
}
