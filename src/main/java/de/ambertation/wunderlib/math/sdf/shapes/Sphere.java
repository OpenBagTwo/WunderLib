package de.ambertation.wunderlib.math.sdf.shapes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

import de.ambertation.wunderlib.math.Float3;
import de.ambertation.wunderlib.math.Transform;
import de.ambertation.wunderlib.math.sdf.SDF;

public class Sphere extends BaseShape {
    public static final Transform DEFAULT_TRANSFORM = Transform.of(Float3.of(0, 0, 0), Float3.of(8, 8, 8));
    public static final MapCodec<Sphere> DIRECT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    Codec.INT.fieldOf("material").orElse(0).forGetter(BaseShape::getMaterialIndex)
            )
            .apply(instance, Sphere::new)
    );

    public static final MapCodec<Sphere> CODEC = KeyDispatchDataCodec.of(DIRECT_CODEC).codec();

    @Override
    public MapCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    public Sphere(Transform t, int matIndex) {
        super(t, matIndex);
    }

    public Sphere(Transform t) {
        this(t, 0);
    }

    public Sphere(Float3 center, double radius) {
        super(Transform.of(center, Float3.of(radius * 2)), 0);
    }


    @Override
    public double dist(Float3 pos) {
        pos = getParentTransformMatrix().inverted().transform(pos);

        return pos.sub(getCenter()).length() - getRadius();
    }

    public double getRadius() {
        return transform.size.min() / 2;
    }

    @Override
    public Transform defaultTransform() {
        return DEFAULT_TRANSFORM;
    }
}
