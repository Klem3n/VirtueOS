/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oldscape.tool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.Constants;
import com.oldscape.tool.cache.Container;
import com.oldscape.tool.cache.FileStore;
import com.oldscape.tool.cache.ReferenceTable;

/**
 * @author Kyle Friz
 * @since Dec 30, 2015
 */
public class ModelDumper {

	public static void main(String[] args) throws IOException {
		File directory = new File(Constants.MODEL_PATH + "/all");

		if (!directory.exists()) {
			directory.mkdirs();
		}

		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			ReferenceTable table = cache.getReferenceTable(7);

			for (int i = 0; i < table.capacity(); i++) {
				if (table.getEntry(i) == null)
					continue;

				Container container = cache.read(7, i);
				byte[] bytes = new byte[container.getData().limit()];
				container.getData().get(bytes);

				try (DataOutputStream dos = new DataOutputStream(
						new FileOutputStream(new File(directory, i + ".dat")))) {
					dos.write(bytes);
				}

				double progress = (double) i / table.capacity() * 100;

				System.out.printf("%.2f%s\n", progress, "%");
			}
		}
	}

}