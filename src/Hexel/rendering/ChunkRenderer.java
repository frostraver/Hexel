package Hexel.rendering;

import Hexel.chunk.Chunk;
import Hexel.chunk.Chunks;

import Hexel.math.Vector3i;
import Hexel.math.Vector2d;
import Hexel.math.HexGeometry;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.ArrayList;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkRenderer {

    private Chunks chunks;

	private Texture ttex;

    private LinkedBlockingQueue<GLChunk> glChunksToLoad = new LinkedBlockingQueue<GLChunk>();
    private HashMap<Vector3i, GLChunk> glChunkTable = new HashMap<Vector3i, GLChunk>();
    public HashMap<Vector3i, GLChunk> getGLChunkTable(){ return glChunkTable; };

    private ExecutorService chunkVertexBufferGeneratorThreadPool = Executors.newFixedThreadPool(8);

    public ChunkRenderer(Chunks chunks) {
        this.chunks = chunks;
    }

    public void init(GL2 gl){
		InputStream stream = null;
		try {
			stream = new FileInputStream(new File("img/atlas.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		TextureData data = null;
		try {
			data = TextureIO.newTextureData(gl.getGLProfile(), stream, false, "png");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		ttex = TextureIO.newTexture(data);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    }

    public void loadChunks(final ArrayList<Vector3i> points){
        if (points.size() == 0)
            return;
        for (int i = 0; i < points.size(); i++){
            Vector3i p = points.get(i);
            if (!hasChunk(p))
                glChunkTable.put(p, null);
        }
        chunkVertexBufferGeneratorThreadPool.execute(new Runnable(){
            @Override
            public void run(){
                for (int i = 0; i < points.size(); i++){
                    Chunk chunk = chunks.getChunk(points.get(i));
                    GLChunk glChunk = new GLChunk(chunk, chunks);
                    glChunk.position = new Vector3i(chunk.cx, chunk.cy, chunk.cz);
                    glChunk.genArrayList();
                    ChunkRenderer.this.loadGLChunk(glChunk);
                }
            }
        });
    }

    public boolean hasChunk(Vector3i pos){
        return glChunkTable.containsKey(pos);
    }

    public void loadGLChunk(GLChunk glChunk){
        glChunksToLoad.add(glChunk);
    }

    public void unloadGLChunk(Vector3i pos){
        glChunkTable.remove(pos);
    }

    public void update(GL2 gl) {
        int i = 0;
        while (!this.glChunksToLoad.isEmpty()){
            GLChunk glChunk = this.glChunksToLoad.poll();
            glChunk.initBuffer(gl);
            glChunkTable.put(glChunk.position, glChunk);
            i++;
            if (i > 10)
                break;
        }
    }

    public void render(GL2 gl){

	    ttex.enable(gl);
		ttex.bind(gl);

        Iterator<Map.Entry<Vector3i, GLChunk>> it = glChunkTable.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Vector3i, GLChunk> entry = it.next();
            Vector3i position = entry.getKey();
            GLChunk glChunk = entry.getValue();

            if (glChunk == null || glChunk.buffer == null)
                continue;

		    gl.glPushMatrix();

            Vector2d hexPosition = new Vector2d();
            HexGeometry.hexToCartesian(position.x, position.y, hexPosition);

            gl.glTranslatef((float)(hexPosition.x*16), (float)(hexPosition.y*16), (float)(position.z*16));

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glChunk.buffer.id[0]);


            //SOLIDS
            gl.glAlphaFunc(GL2.GL_EQUAL, 1.0f);
            gl.glEnable(GL2.GL_ALPHA_TEST);

            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

            gl.glVertexPointer(3, GL2.GL_FLOAT, 9*4, 0);
            gl.glTexCoordPointer(2, GL2.GL_FLOAT, 9*4, 3*4);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, glChunk.buffer.size/(4*9));

            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

            gl.glDisable(GL2.GL_ALPHA_TEST);

		    gl.glPopMatrix();

        }
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        it = glChunkTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Vector3i, GLChunk> entry = it.next();
            Vector3i position = entry.getKey();
            GLChunk glChunk = entry.getValue();

            if (glChunk == null || glChunk.buffer == null)
                continue;

		    gl.glPushMatrix();

            Vector2d hexPosition = new Vector2d();
            HexGeometry.hexToCartesian(position.x, position.y, hexPosition);

            gl.glTranslatef((float)(hexPosition.x*16), (float)(hexPosition.y*16), (float)(position.z*16));

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glChunk.buffer.id[0]);


            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

            gl.glVertexPointer(3, GL2.GL_FLOAT, 9*4, 0);
            gl.glColorPointer(4, GL2.GL_FLOAT, 9*4, 5*4);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, glChunk.buffer.size/(4*9));

            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);


		    gl.glPopMatrix();

        }

        it = glChunkTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Vector3i, GLChunk> entry = it.next();
            Vector3i position = entry.getKey();
            GLChunk glChunk = entry.getValue();

            if (glChunk == null || glChunk.buffer == null)
                continue;

		    gl.glPushMatrix();

            Vector2d hexPosition = new Vector2d();
            HexGeometry.hexToCartesian(position.x, position.y, hexPosition);

            gl.glTranslatef((float)(hexPosition.x*16), (float)(hexPosition.y*16), (float)(position.z*16));

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glChunk.buffer.id[0]);

            //NOT SOLIDS
            gl.glAlphaFunc(GL2.GL_NOTEQUAL, 1.0f);
            gl.glEnable(GL2.GL_ALPHA_TEST);

            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

            gl.glVertexPointer(3, GL2.GL_FLOAT, 9*4, 0);
            gl.glTexCoordPointer(2, GL2.GL_FLOAT, 9*4, 3*4);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, glChunk.buffer.size/(4*9));

            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

            gl.glDisable(GL2.GL_ALPHA_TEST);
		    gl.glPopMatrix();

        }
        gl.glDisable(GL2.GL_BLEND);
		ttex.disable(gl);
    }
}

