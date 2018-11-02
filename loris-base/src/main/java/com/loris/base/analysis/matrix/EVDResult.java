package com.loris.base.analysis.matrix;

import Jama.Matrix;

public class EVDResult
{
	public Matrix d;
	public Matrix v;

	public EVDResult(Matrix d, Matrix v)
	{
		this.d = d;
		this.v = v;
	}
}
