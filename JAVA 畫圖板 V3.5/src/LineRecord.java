class LineRecord 
{
	int firstIndex;
	int lastIndex;
	int delta;
	public LineRecord(int first,int last)
	{
		firstIndex=first;
		lastIndex=last;
		delta=last-first+1;
	}
}
