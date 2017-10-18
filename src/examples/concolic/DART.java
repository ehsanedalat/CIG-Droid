/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package concolic;

//import java.util.concurrent.ThreadLocalRandom;
//import gov.nasa.jpf.symbc.Symbolic;
//import gov.nasa.jpf.symbc.*;
// taken from the original DART paper

public class DART {
//@Symbolic ("true")
//static int a;
public static void test(int x, int y) {
		if (y>=5+x && x > 0){
			if(x>0 && y==10) {
				//while(x>3)
				//	x++;
				abort();
			}
		} else {
			if (x==-1)
				abort();
			else
				y=100;
		}

//@Symbolic ("true")
//int 
//int a = ThreadLocalRandom.current().nextInt(-1000, 1000 + 1);
//Integer a=(Integer)Debug.makeSymbolicRef("a",new Integer(2));
//int a=(int)Math.random();
//if(a>=0){abort();}
		

	}

    public static void abort() {
    	assert(false);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test(-2,100);
		//test();
	}
}
